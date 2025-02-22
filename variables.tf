variable "domain_name" {
  type        = string
  description = "turdiu.eu"
}

variable "cloudflare_api_token" {
  type        = string
  description = "Cloudflare API token"
  sensitive   = true
}

variable "cloudflare_zone_id" {
  type        = string
  description = "Cloudflare Zone ID"
}

variable "environment" {
  type        = string
  description = "Environment (dev/prod)"
  default     = "dev"
}

variable "scores_lambda" {
  type        = string
  description = "nba_scores_lambda"
}

