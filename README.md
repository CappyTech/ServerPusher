# ServerPusher

Generic JSON transport library for Minecraft plugins.

ServerPusher enables Spigot/Paper plugins to send dynamic JSON payloads via HTTP POST to external services like Node.js backends. It’s lightweight, plugin-agnostic, and ideal for dashboards, monitoring, and analytics.

---

## Features

- Send structured JSON from Minecraft plugins
- Built-in HTTP client with custom headers
- Reusable as a library across plugins
- Used by [ServerEmitter](https://github.com/CappyTech/ServerEmitter) and [ServerReceiver](https://github.com/CappyTech/ServerReceiver)

---

## Usage

1. Add the `ServerPusher-1.0.1.jar` to your plugin’s `/lib` folder
2. Reference it in your plugin's `pom.xml`:

```xml
<dependency>
  <groupId>com.sovereigncraft</groupId>
  <artifactId>ServerPusher</artifactId>
  <version>1.0.0</version>
  <scope>system</scope>
  <systemPath>${project.basedir}/lib/serverpusher-1.0.0.jar</systemPath>
</dependency>
```

3. Initialize and use ServerPusher in your plugin:

```java
Map<String, String> headers = new HashMap<>();
headers.put("Authorization", "Bearer your-token");

ServerPusher.configure("http://localhost:3000/api/push", headers, getLogger());
```

4. Send data from anywhere:

```java
Map<String, Object> payload = new HashMap<>();
payload.put("event", "heartbeat");
payload.put("players", Bukkit.getOnlinePlayers().size());
ServerPusher.sendData("heartbeat", payload);
```

---

## Example Plugin

[ServerEmitter](https://github.com/CappyTech/ServerEmitter) uses ServerPusher to test and emit data to a backend.

---

## License

MIT

```
Copyright 2025 CappyTech (https://github.com/CappyTech/)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

```
