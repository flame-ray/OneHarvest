# OneHarvest

> 面向 Paper / Spigot 1.21.x 服务端的 Minecraft 采集增强插件项目。

OneHarvest 的目标是为 Minecraft 生存服务器提供连锁挖矿、连锁砍树和范围挖掘等高效采集能力，并允许玩家按需切换功能与调整采集范围。

## 当前状态

当前公开版本为 **v1.1.0**，项目仍处于基础开发阶段。

目前源码已经完成：

- Paper / Spigot 1.21 API 的插件基础配置
- Java 21 / Maven 构建配置
- 插件加载与关闭日志

> 重要：当前源码尚未注册指令、事件监听器或实际采集逻辑。因此连锁挖矿、连锁砍树、范围挖掘、配置文件与 `/oh` 指令均尚未实现。请勿将 v1.1.0 用于依赖这些功能的正式服务器。

## 安装

1. 从 [Releases](https://github.com/flame-ray/OneHarvest/releases) 下载最新 JAR。
2. 将 JAR 文件放入 Minecraft 服务端的 `plugins` 文件夹。
3. 使用 Java 21 启动 Paper 或 Spigot 1.21.x 服务端。
4. 在服务器控制台确认出现 `OneHarvest enabled!`。

## 当前可验证功能

| 功能 | 状态 |
| --- | --- |
| 被 Paper / Spigot 识别并加载 | 已实现 |
| 启动 / 关闭日志 | 已实现 |
| `/oh` 指令 | 未实现 |
| 连锁挖矿 | 未实现 |
| 连锁砍树 | 未实现 |
| 范围挖掘 | 未实现 |
| 配置与热重载 | 未实现 |

## 开发计划

下一个功能版本计划按以下顺序推进：

1. 实现 `/oh help`、`/oh toggle`、`/oh range`、`/oh max`、`/oh reload` 指令。
2. 添加方块破坏事件监听器与安全的连锁搜索上限。
3. 实现矿脉挖掘、树木识别与 1×1 / 3×3 / 5×5 / 7×7 范围挖掘。
4. 支持权限节点、掉落物、经验、精准采集和时运附魔。
5. 添加 `config.yml`、测试与性能验证后再发布新版本。

## 从源码构建

### 环境要求

- Java 21
- Maven 3.8+
- Paper 或 Spigot 1.21.x 测试服务端

### 构建命令

```bash
mvn package
```

构建完成后的 JAR 位于 `target/` 目录。

## 参与贡献

欢迎通过 [Issues](https://github.com/flame-ray/OneHarvest/issues) 提交 Bug 报告、功能建议和兼容性反馈。提交 Pull Request 前，请确保项目可使用 Java 21 与 Maven 正常构建。

## 许可证

本项目采用 [MIT License](LICENSE) 开源。
