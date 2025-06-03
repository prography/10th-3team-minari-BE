#!/bin/bash
set -e

echo "🔐 SSM에서 설정값 조회"
export SPRING_DATASOURCE_URL=$(aws ssm get-parameter --name "/minari/db/url" --with-decryption --query "Parameter.Value" --output text)
export SPRING_DATASOURCE_USERNAME=$(aws ssm get-parameter --name "/minari/db/username" --with-decryption --query "Parameter.Value" --output text)
export SPRING_DATASOURCE_PASSWORD=$(aws ssm get-parameter --name "/minari/db/pw" --with-decryption --query "Parameter.Value" --output text)

export KAKAO_REST_API_KEY=$(aws ssm get-parameter --name "/minari/oauth/kakao/key" --with-decryption --query "Parameter.Value" --output text)
export KAKAO_SECRET_KEY=$(aws ssm get-parameter --name "/minari/oauth/kakao/secret" --with-decryption --query "Parameter.Value" --output text)
export KAKAO_REDIRECT_URI=$(aws ssm get-parameter --name "/minari/oauth/kakao/redirect_uri" --with-decryption --query "Parameter.Value" --output text)

export NAVER_CLIENT_ID=$(aws ssm get-parameter --name "/minari/naver/id" --with-decryption --query "Parameter.Value" --output text)
export NAVER_CLIENT_SECRET=$(aws ssm get-parameter --name "/minari/naver/secret" --with-decryption --query "Parameter.Value" --output text)

export JWT_SECRET=$(aws ssm get-parameter --name "/minari/jwt/secret" --with-decryption --query "Parameter.Value" --output text)

export ECR_URI=$(aws ssm get-parameter --name "/minari/prod/ECR_URI" --query "Parameter.Value" --output text)

export MAIL_USERNAME=$(aws ssm get-parameter --name "/minari/mail/username" --query "Parameter.Value" --output text)
export MAIL_PASSWORD=$(aws ssm get-parameter --name "/minari/mail/password" --query "Parameter.Value" --output text)


echo "🔑 ECR 로그인"
aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin $ECR_URI

echo "📥 도커 이미지 Pull"
docker pull $ECR_URI:latest

echo "🧼 기존 컨테이너 정리"
docker stop minari || true
docker rm minari || true

echo "🚀 컨테이너 실행 (환경변수 주입)"
docker run -d --name minari \
  -e SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL \
  -e SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME \
  -e SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD \
  -e KAKAO_REST_API_KEY=$KAKAO_REST_API_KEY \
  -e KAKAO_SECRET_KEY=$KAKAO_SECRET_KEY \
  -e KAKAO_REDIRECT_URI=$KAKAO_REDIRECT_URI \
  -e NAVER_CLIENT_ID=$NAVER_CLIENT_ID \
  -e NAVER_CLIENT_SECRET=$NAVER_CLIENT_SECRET \
  -e JWT_SECRET=$JWT_SECRET \
  -e MAIL_USERNAME=$MAIL_USERNAME \
  -e MAIL_PASSWORD=$MAIL_PASSWORD \
  -p 8080:8080 \
  $ECR_URI:latest