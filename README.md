# OneHarvest

> 面向 Paper / Spigot 1.21.x 服务端的 Minecraft 采集增强插件。

OneHarvest 为生存服务器提供可控制的连锁挖矿、连锁砍树和范围挖掘。所有自动破坏都会保留为正常的玩家方块破坏事件，因此保护插件可以照常取消受保护区域中的操作。

## 当前开发版本

`main` 分支当前为 **1.2.0-SNAPSHOT**，已通过 Java 21 编译与规则单元测试。稳定版 Release 会在 Paper / Spigot 实服回归验证后发布。

## 功能

- **连锁挖矿**：破坏矿石时，自动采集相邻的同类矿石。
- **连锁砍树**：破坏原木、菌柄或菌核时，自动采集相连的同类木质方块。
- **范围挖掘**：对石头、圆石、深板岩、泥土、砂砾、凝灰岩等常见方块，支持 1×1 / 3×3 / 5×5 / 7×7 范围。
- **个人设置**：每位玩家可以独立开关自动采集、保存范围偏好。
- **安全上限**：全局限制每次自动破坏的方块数量，默认 10,000。
- **保护兼容**：自动破坏通过玩家方块破坏流程执行，其他插件可以取消该事件。
- **热重载**：管理员可以重新读取全局配置与玩家设置。

## 安装

### 使用 Release

从 [Releases](https://github.com/flame-ray/OneHarvest/releases) 下载已经验证的稳定 JAR，放入服务端的 `plugins` 文件夹后重启服务器。

### 使用开发版本

当前功能实现位于 `main` 分支。安装 Java 21 和 Maven 后执行：

```bash
mvn package
```

将 `target/OneHarvest-1.2.0-SNAPSHOT.jar` 放入 `plugins` 文件夹，并使用 Paper 或 Spigot 1.21.x 启动服务器。

## 指令

| 指令 | 说明 |
| --- | --- |
| `/oh help` | 查看命令帮助。 |
| `/oh toggle` | 开启或关闭自己的自动采集。 |
| `/oh range <0-3>` | 设置范围挖掘：0=1×1，1=3×3，2=5×5，3=7×7。 |
| `/oh max <1-100000>` | 设置全局单次自动破坏上限（管理员）。 |
| `/oh reload` | 重载全局配置和玩家设置（管理员）。 |

## 权限

| 权限节点 | 默认值 | 说明 |
| --- | --- | --- |
| `oneharvest.use` | 所有玩家 | 使用自动采集能力。 |
| `oneharvest.admin` | OP | 修改全局自动破坏上限。 |
| `oneharvest.reload` | OP | 重载配置。 |
| `oneharvest.*` | OP | 授予全部权限。 |

## 配置

首次运行后会生成 `plugins/OneHarvest/config.yml`：

```yaml
enabled: true
max-chain-break: 10000
vein-mining: true
tree-felling: true
area-mining: true
default-range: 1
```

- `enabled`：总开关。
- `max-chain-break`：一次自动采集最多额外破坏的方块数量，取值为 1 至 100000。
- `vein-mining`、`tree-felling`、`area-mining`：三类功能开关。
- `default-range`：新玩家的默认范围，0 至 3 分别对应 1×1 至 7×7。

修改配置后使用 `/oh reload` 生效。玩家偏好保存在 `plugins/OneHarvest/players.yml`。

## 构建与测试

### 环境要求

- Java 21
- Maven 3.8+
- Paper 或 Spigot 1.21.x 测试服务端

### 验证命令

```bash
mvn clean test package
```

该命令会运行规则单元测试并生成插件 JAR。

## 实服测试清单

发布稳定版前，请在测试服务器验证：

1. 煤矿、钻石矿、下界石英矿等连锁挖矿的边界与上限。
2. 各类原木、下界菌柄的连锁砍树效果。
3. 三个朝向下的 3×3、5×5、7×7 范围挖掘。
4. WorldGuard 等保护插件能否取消自动破坏。
5. 精准采集、时运、耐久与服务器性能表现。

## 参与贡献

欢迎通过 [Issues](https://github.com/flame-ray/OneHarvest/issues) 提交 Bug 报告、功能建议和兼容性反馈。提交 Pull Request 前请运行 `mvn clean test package`。

## 许可证

本项目采用 [MIT License](LICENSE) 开源。
