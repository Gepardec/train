FROM alpine

RUN apk add --no-cache jq openssh-keygen
RUN apk add "opentofu@community" "--repository=@community https://dl-cdn.alpinelinux.org/alpine/edge/community"
WORKDIR /opt/train
COPY . .
ENTRYPOINT [ "./entrypoint.sh" ]
