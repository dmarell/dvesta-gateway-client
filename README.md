# dvesta-gateway-client
System client library for dvesta-gateway. Connnects via web sockets to gateway and listens for commands.

## Release notes
* Version 1.0.1 - 2016-03-17
  * New groupId
* Version 1.0.0 - 2016-03-17
  * First release

## Maven usage

```
<repositories>
  <repository>
    <id>marell</id>
    <url>http://marell.se/artifactory/libs-release</url>
  </repository>
</repositories>
...
<dependency>
  <groupId>se.marell</groupId>
  <artifactId>dvesta-gateway-client</artifactId>
  <version>1.0.1</version>
</dependency>
...
```

## Usage

Create a bean implementing GatewayCommandListener:

```
@Configuration
@Import(DvestaGatewayClientSpringConfig.class)
class MyConfig {
}

@Component
class Hg31GatewayCommandListener implements GatewayCommandListener {
    @Override
    public void command(CommandResponseSender sender, String messageId, String commandName, List<String> commandArgs) {
        sender.sendResponse(messageId, "Received command: " + commandName);
    }
}
```
