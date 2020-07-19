# Nightgown Reader

Nightgown reader is to read the advertisement data containing the sensor data from the ESP32 [Nightgown](https://github.com/LilDataMonster/nightgown) Project.

## Dependencies

The TinyB library is used to use the bluetooth adapter to communicate to other bluetooth devices. Compile and install the library with the java bindings: `-DBUILDJAVA=ON`.

[Tiny Bluetooth LE Library](https://github.com/intel-iot-devkit/tinyb)

The `tinyb.jar` must also be added to the build project.

## VM Arguments

If the tinyb library is not in the path, it can be added using the following VM arguments to the JVM. Additionally, the simple formatter can be used for logging to represent data with `-Djava.util.logging.SimpleFormatter.format`.

The tinyb library path must contain the following shared object files:

- `libjavatiny.so`
- `libjavatinyb.so.0`
- `libjavatinyb.so.0.5.0`

```
-Djava.library.path=/usr/java/packages/lib:/usr/lib/x86_64-linux-gnu/jni:/lib/x86_64-linux-gnu:/usr/lib/x86_64-linux-gnu:/usr/lib/jni:/lib:/usr/lib:/usr/local/lib -Djava.util.logging.SimpleFormatter.format='[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS] [%4$s] [%2$s] %5$s%6$s%n'
```
