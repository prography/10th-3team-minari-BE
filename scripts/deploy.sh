#!/bin/bash

echo "ssm에서 애플리케이션 실행시 필요한 설정값 조회"
export MINARI_DB_URL=$(aws ssm get-parameter --name "/minari/db/url" --with-decryption --query "Parameter.Value" --output text)
export MINARI_DB_USERNAME=$(aws ssm get-parameter --name "/minari/db/username" --with-decryption --query "Parameter.Value" --output text)
export MINARI_DB_PASSWORD=$(aws ssm get-parameter --name "/minari/db/pw" --with-decryption --query "Parameter.Value" --output text)
export KAKAO_REST_API_KEY=$(aws ssm get-parameter --name "/minari/oauth/kakao/key" --with-decryption --query "Parameter.Value" --output text)
export KAKAO_SECRET_KEY=$(aws ssm get-parameter --name "/minari/oauth/kakao/secret" --with-decryption --query "Parameter.Value" --output text)
export KAKAO_REDIRECT_URI=$(aws ssm get-parameter --name "/minari/oauth/kakao/redirect_uri" --with-decryption --query "Parameter.Value" --output text)
export NAVER_CLIENT_ID=$(aws ssm get-parameter --name "/minari/naver/id" --with-decryption --query "Parameter.Value" --output text)
export NAVER_CLIENT_SECRET=$(aws ssm get-parameter --name "/minari/naver/secret" --with-decryption --query "Parameter.Value" --output text)
export JWT_SECRET=$(aws ssm get-parameter --name "/minari/jwt/secret" --with-decryption --query "Parameter.Value" --output text)
export ECR_URI=$(aws ssm get-parameter --name "/minari/prod/ECR_URI" --query "Parameter.Value" --output text)

echo "ECR 로그인"
aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin $ECR_URI

echo "도커 이미지 pull"
docker pull $ECR_URI:latest

echo "기존 컨테이너 종료"
docker stop minari || true
docker rm minari || true

echo "컨테이너 실행 (환경변수 주입)"
docker run -d --name minari \
  -e /minari/db/url=$MINARI_DB_URL \
  -e /minari/db/username=$MINARI_DB_USERNAME \
  -e /minari/db/pw=$MINARI_DB_PASSWORD \
  -e /minari/oauth/kakao/key=$KAKAO_REST_API_KEY \
  -e /minari/oauth/kakao/secret=$KAKAO_SECRET_KEY \
  -e /minari/oauth/kakao/redirect_uri=$KAKAO_REDIRECT_URI \
  -e /minari/naver/id=$NAVER_CLIENT_ID \
  -e /minari/naver/secret=$NAVER_CLIENT_SECRET \
  -e /minari/jwt/secret=$JWT_SECRET \
  -p 8080:8080 \
  $ECR_URI:latest