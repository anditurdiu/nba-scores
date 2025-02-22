# Configre AWS Provide 
provider "aws" {
  region = "eu-central-1"
}
resource "aws_iam_role" "lambda_role" {
  name = "nba-scores_lambda_execution_role"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

provider "cloudflare" {
  api_token = var.cloudflare_api_token
}


resource "aws_iam_policy_attachment" "lambda_logs" {
  name       = "nba-scores_lambda_logs"
  roles      = [aws_iam_role.lambda_role.name]
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

resource "aws_lambda_function" "nba_scores_lambda" {
  filename      = "nba-scores-lambda\\build\\libs\\nba-scores-aws-1.0-SNAPSHOT-all.jar"
  function_name = "nba-scores"
  role          = aws_iam_role.lambda_role.arn
  handler       = "org.ndia.products.Handler::handleRequest"
  runtime       = "java21" # Kotlin runs on JVM
  memory_size   = 128
  timeout       = 10
}

resource "aws_api_gateway_rest_api" "nba_scores_api" {
  name        = "nba-scores-api-${var.environment}"
  description = "API Gateway for NBA Scores Lambda"

  tags = {
    Name        = "nba-scores-api"
    Environment = var.environment
  }
}

resource "aws_api_gateway_resource" "scores" {
  rest_api_id = aws_api_gateway_rest_api.nba_scores_api.id
  parent_id   = aws_api_gateway_rest_api.nba_scores_api.root_resource_id
  path_part   = "scores"
}

resource "aws_api_gateway_method" "get_scores" {
  rest_api_id   = aws_api_gateway_rest_api.nba_scores_api.id
  resource_id   = aws_api_gateway_resource.scores.id
  http_method   = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "lambda_integration" {
  rest_api_id             = aws_api_gateway_rest_api.nba_scores_api.id
  resource_id             = aws_api_gateway_resource.scores.id
  http_method             = aws_api_gateway_method.get_scores.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.scores_lambda.invoke_arn
}

# CORS Configuration
resource "aws_api_gateway_method" "scores_options" {
  rest_api_id   = aws_api_gateway_rest_api.nba_scores_api.id
  resource_id   = aws_api_gateway_resource.scores.id
  http_method   = "OPTIONS"
  authorization = "NONE"
}

resource "aws_lambda_permission" "apigw_lambda" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.nba_scores_lambda.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_api_gateway_rest_api.nba_scores_api.execution_arn}/*/*"
}

output "api_gateway_url" {
  value = "${aws_api_gateway_deployment.nba_scores_deployment.invoke_url}/execute"
}

resource "aws_api_gateway_method_response" "scores_options" {
  rest_api_id = aws_api_gateway_rest_api.nba_scores_api.id
  resource_id = aws_api_gateway_resource.scores.id
  http_method = aws_api_gateway_method.scores_options.http_method
  status_code = "200"

  response_parameters = {
    "method.response.header.Access-Control-Allow-Headers" = true
    "method.response.header.Access-Control-Allow-Methods" = true
    "method.response.header.Access-Control-Allow-Origin"  = true
  }
}

resource "aws_api_gateway_integration_response" "scores_options" {
  rest_api_id = aws_api_gateway_rest_api.nba_scores_api.id
  resource_id = aws_api_gateway_resource.scores.id
  http_method = aws_api_gateway_method.scores_options.http_method
  status_code = aws_api_gateway_method_response.scores_options.status_code

  response_parameters = {
    "method.response.header.Access-Control-Allow-Headers" = "'Content-Type,X-Amz-Date,Authorization,X-Api-Key'"
    "method.response.header.Access-Control-Allow-Methods" = "'GET,OPTIONS'"
    "method.response.header.Access-Control-Allow-Origin"  = "'*'"
  }
}

# API Gateway Deployment
resource "aws_api_gateway_deployment" "nba_scores_api_deployment" {
  rest_api_id = aws_api_gateway_rest_api.nba_scores_api.id

  depends_on = [
    aws_api_gateway_integration.lambda_integration,
    aws_api_gateway_integration_response.scores_options
  ]

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_api_gateway_stage" "nba_scores_api_stage" {
  deployment_id = aws_api_gateway_deployment.nba_scores_api_deployment.id
  rest_api_id   = aws_api_gateway_rest_api.nba_scores_api.id
  stage_name    = var.environment
}


# S3 bucket for static website
resource "aws_s3_bucket" "scores_fe" {
  bucket = "scores_fe-${var.environment}"

  tags = {
    Name        = "scores_fe"
    Environment = var.environment
  }
}

resource "aws_s3_bucket_public_access_block" "scores_fe" {
  bucket = aws_s3_bucket.scores_fe.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

# CloudFront distribution
resource "aws_cloudfront_distribution" "scores_fe" {
  enabled             = true
  default_root_object = "index.html"
  price_class         = "PriceClass_100" # Use only North America and Europe endpoints to reduce costs

  origin {
    domain_name = aws_s3_bucket.scores_fe.bucket_regional_domain_name
    origin_id   = "S3-${aws_s3_bucket.scores_fe.bucket}"

    s3_origin_config {
      origin_access_identity = aws_cloudfront_origin_access_identity.nba_oai.cloudfront_access_identity_path
    }
  }

  # Custom error response to handle React Router
  custom_error_response {
    error_code         = 403
    response_code      = 200
    response_page_path = "/index.html"
  }

  custom_error_response {
    error_code         = 404
    response_code      = 200
    response_page_path = "/index.html"
  }

  default_cache_behavior {
    allowed_methods        = ["GET", "HEAD"]
    cached_methods         = ["GET", "HEAD"]
    target_origin_id       = "S3-${aws_s3_bucket.scores_fe.bucket}"
    viewer_protocol_policy = "redirect-to-https"
    compress               = true

    forwarded_values {
      query_string = false
      cookies {
        forward = "none"
      }
    }

    min_ttl     = 0
    default_ttl = 3600  # 1 hour
    max_ttl     = 86400 # 24 hours
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  tags = {
    Name        = "nba-scores-cdn"
    Environment = var.environment
  }
}

resource "aws_cloudfront_origin_access_identity" "nba_oai" {
  comment = "OAI for NBA Scores website"
}

# Cloudflare DNS Record
resource "cloudflare_record" "scores_fe" {
  zone_id = var.cloudflare_zone_id
  name    = var.domain_name
  value   = aws_cloudfront_distribution.scores_fe.domain_name
  type    = "CNAME"
  proxied = true
}

# outputs.tf
output "api_gateway_url" {
  value = "${aws_api_gateway_stage.nba_api_stage.invoke_url}/scores"
}

output "cloudfront_domain" {
  value = aws_cloudfront_distribution.scores_fe.domain_name
}

output "fe_bucket_name" {
  value = aws_s3_bucket.scores_fe.bucket
}
