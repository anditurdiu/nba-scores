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

resource "aws_iam_policy_attachment" "lambda_logs" {
  name       = "nba-scores_lambda_logs"
  roles      = [aws_iam_role.lambda_role.name]
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

resource "aws_lambda_function" "nba_scores_lambda" {
  filename      = "nba-scores.zip"
  function_name = "nba-scores"
  role          = aws_iam_role.lambda_role.arn
  handler       = "nba.scores.Handler::handleRequest"
  runtime       = "java17"  # Kotlin runs on JVM
  memory_size   = 128
  timeout       = 10
}

resource "aws_api_gateway_rest_api" "nba_scores_api" {
  name        = "NBA Scores API"
  description = "API Gateway for NBA Scores Lambda"
}

resource "aws_api_gateway_resource" "nba_scores_resource" {
  rest_api_id = aws_api_gateway_rest_api.nba_scores_api.id
  parent_id   = aws_api_gateway_rest_api.nba_scores_api.root_resource_id
  path_part   = "execute"
}

resource "aws_api_gateway_method" "nba_scores_method" {
  rest_api_id   = aws_api_gateway_rest_api.nba_scores_api.id
  resource_id   = aws_api_gateway_resource.nba_scores_resource.id
  http_method   = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "nba_scores_integration" {
  rest_api_id = aws_api_gateway_rest_api.nba_scores_api.id
  resource_id = aws_api_gateway_resource.nba_scores_resource.id
  http_method = aws_api_gateway_method.nba_scores_method.http_method
  integration_http_method = "POST"
  type        = "AWS_PROXY"
  uri         = aws_lambda_function.nba_scores_lambda.invoke_arn
}

resource "aws_api_gateway_deployment" "nba_scores_deployment" {
  depends_on = [aws_api_gateway_integration.nba_scores_integration]
  rest_api_id = aws_api_gateway_rest_api.nba_scores_api.id
  stage_name  = "prod"
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