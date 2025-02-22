provider "aws" {
  region = "eu-central-1"
}

resource "aws_iam_role" "lambda_exec" {
  name = "get-scores_lambda_execution_role"

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

resource "aws_iam_policy_attachment" "lambda_logs" {
  name       = "get_scores_lambda_logs"
  roles      = [aws_iam_role.lambda_exec.name]
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

resource "aws_iam_role_policy_attachment" "lambda_basic_execution" {
  role       = aws_iam_role.lambda_exec.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

resource "aws_lambda_function" "get_scores_lambda" {
  filename      = "nba-scores-lambda\\build\\libs\\nba-scores-aws-1.0-SNAPSHOT-all.jar"
  function_name = "get-scores"
  role          = aws_iam_role.lambda_exec.arn
  handler       = "org.ndia.products.Handler::handleRequest"
  runtime       = "java21" # Kotlin runs on JVM
  memory_size   = 128
  timeout       = 60

  environment {
    variables = {
      KEY = "VALUE"
    }
  }

  snap_start {
    apply_on = "PublishedVersions"
  }
}

resource "aws_api_gateway_rest_api" "mouting_scores_api" {
  name        = "mouting-scores-app"
  description = "API Gateway for the Mouting Scores App"
}

resource "aws_api_gateway_resource" "scores_resource" {
  rest_api_id = aws_api_gateway_rest_api.mouting_scores_api.id
  parent_id   = aws_api_gateway_rest_api.mouting_scores_api.root_resource_id
  path_part   = "scores"
}

resource "aws_api_gateway_method" "get_method" {
  rest_api_id   = aws_api_gateway_rest_api.mouting_scores_api.id
  resource_id   = aws_api_gateway_resource.scores_resource.id
  http_method   = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "lambda_integration" {
  rest_api_id             = aws_api_gateway_rest_api.mouting_scores_api.id
  resource_id             = aws_api_gateway_resource.scores_resource.id
  http_method             = aws_api_gateway_method.get_method.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.get_scores_lambda.invoke_arn
}

resource "aws_api_gateway_deployment" "api_deployment" {
  depends_on  = [aws_api_gateway_integration.lambda_integration]
  rest_api_id = aws_api_gateway_rest_api.mouting_scores_api.id
  stage_name  = "prod"
}

resource "aws_lambda_permission" "apigw_lambda" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.get_scores_lambda.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_api_gateway_rest_api.mouting_scores_api.execution_arn}/*/*"
}

output "api_endpoint" {
  value = aws_api_gateway_deployment.api_deployment.invoke_url
}
