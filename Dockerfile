FROM alpine

RUN apk add --no-cache terraform jq openssh-keygen
WORKDIR /opt/train
COPY . .
ENTRYPOINT [ "./entrypoint.sh" ]