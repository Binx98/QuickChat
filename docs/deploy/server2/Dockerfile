# 基础镜像：构建镜像依赖
FROM openjdk:17

# 维护者
MAINTAINER 徐志斌

# 宿主机文件 COPY 镜像（类似COPY，不过COPY不能自动解压文件，不能访问网络资源）
ADD quick-chat.jar /quick-chat.jar

# 运行成容器时的端口
EXPOSE 8888

# 容器启动时执行（类似CMD）
ENTRYPOINT ["java", "-jar", "quick-chat.jar"]