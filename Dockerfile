FROM    openjdk:8-jdk-alpine
WORKDIR /opt/sai/elevator_content_server
COPY    elevator_content_server*.jar ./
COPY    script/start.sh ./
# 设置时区
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories
RUN apk add --no-cache tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    &&rm -rf /var/cache/apk/* /tmp/* /var/tmp/* $HOME/.cache

CMD  sh start.sh