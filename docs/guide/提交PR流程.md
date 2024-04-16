本教程只做开源代码库Github工程提交pr的教程，不做其他的深入的讲解
Github和Gitlab的操作类似，只不过Github叫PR，GitLab叫MR，基本上做法是一致的

以开源项目QuickChat为例

https://github.com/Binx98/QuickChat
https://github.com/Binx98/QuickChat-Front

# Fork仓库
进入到项目首页，点进去
![在这里插入图片描述](./images/52da01c64038499a9c617e1e31162236.png)

进入到开源项目内部，Fork一份仓库
![在这里插入图片描述](./images/e31a2f1e468b41bc987d55ddb30c0415.png)

Fork操作的一些选项

![在这里插入图片描述](./images/04ae31df9b954bd5877454c719b738ec.png)

![在这里插入图片描述](./images/93e53e3ae2bd4490a7e0455240d63aa5.png)



# 本地拉取代码 & 远程推送配置
## 本地拉取代码
选一个文件夹，准备拉取代码，注意，此时的代码是从主库中下载的，而并非自己的fork库拉取，fork库有自己的用途，这个后面会讲解

这里我选的是ssh的方式下载代码，clone代码不在讲解范围内，具体请自行百度
![在这里插入图片描述](./images/59215af56edc4aa89a8dda8a57decc23.png)



下载代码，直接下载dev分支的代码

```
git clone -b dev 源仓库远程仓库代码
```

![image-20240414191745575](C:\Users\Barry.chen\AppData\Roaming\Typora\typora-user-images\image-20240414191745575.png)

## 远程推送配置

分别设置upstream，用来获取最新代码。以及自己的仓库origin，作为推送的 “中转”，为pr做准备

先查看当前配置了有哪些分支，这里发现origin配置的是远程分支，改名为upstream，建议删除后重新添加。

![在这里插入图片描述](./images/b27f255816854074b8afdaa1c9ebbcad.png)

删除origin的仓库

```bash
git remote remove origin
```

![在这里插入图片描述](./images/277021c85e544bc190b560cd823a7b29.png)



**重新配置远程推送**

```bash
git remote add 别名 仓库路径
```


![在这里插入图片描述](./images/6cc48c2e8fda450cb78914506b18d575.png)

到这里仓库的基本配置就结束了

# PR演示
## 获取最新代码

```bash
# 暂存代码
git stash
# 从远程仓库获取代码
git pull upstream 分支名
# 恢复暂存代码
git stash pop
```

![在这里插入图片描述](./images/b0b1977cfab24fdcb2724a5b720c2b08.png)



## 修改文件并commit

注意当前一定是dev分支

![在这里插入图片描述](./images/37a1e2f11dc642559c031b6168f9f286.png)

修改文件

![在这里插入图片描述](./images/1549a7a682614972883c48990c4c0dab.png)



![在这里插入图片描述](./images/278a9aff1cff417b87dfe3f014245066.png)

commit完成

![在这里插入图片描述](./images/52c07e2a2244435ea28cb91918da03b8.png)



## 提交代码到origin仓库

点击push，查看提交情况

![在这里插入图片描述](./images/cae11a506c2a4c018e5b7ba3538a4f12.png)



![在这里插入图片描述](./images/f0559af942e64c5f8f4b4815f50da882.png)



![在这里插入图片描述](./images/cfa7e96cb48c4ddf9b9a49119bf84057.png)

**必须先提交到origin仓库上，禁止禁止禁止直接提交到upstream！！！
必须先提交到origin仓库上，禁止禁止禁止直接提交到upstream！！！
必须先提交到origin仓库上，禁止禁止禁止直接提交到upstream！！！**

![在这里插入图片描述](./images/a5b808af78454c8c90fd036c2946eca9.png)



## 发起PR

**回到自己的仓库**，看dev分支，已经看到了刚刚的commit操作已经到了自己的仓库

一定是自己的仓库才能发起PR

![在这里插入图片描述](./images/9f8fec04d4d44b17a643416a2b2e0319.png)



![在这里插入图片描述](./images/14a33ba7ba3a405c8b87cba53e121911.png)

![在这里插入图片描述](./images/351392fb5b384e89b4d8dff057b65e07.png)



**PR选项，选择分支**



![在这里插入图片描述](./images/5c06d38bdbb3487cb4e2def9cd0fe869.png)

![在这里插入图片描述](./images/4f81a25ecca74e9fabbd7172e971a73b.png)

填写PR信息

![在这里插入图片描述](./images/912a885fc1a3459d8dcd67c328c5d87c.png)



创建PR成功

![在这里插入图片描述](./images/32383e2ca264426ca4e8e394c973d10d.png)



如果想关闭PR，拖到下面有选项



![在这里插入图片描述](./images/c0e3367d12d84f7e99ef89a769d7952f.png)



到这里就算是完成了，剩下的就是等review者review完毕后，merge代码，重新执行pull upstream dev的操作，获取最新远端代码，即可。

