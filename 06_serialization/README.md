## Serialisation

For using Proto first following commands need to be executed in project root directory:
```shell
protoc -I=./src/main/proto --cpp_out=./src/main/cpp ./src/main/proto/measurements.proto
```
For using Avro first following commands need to be executed in /src/main/cpp/ directory:
```shell
avrogencpp -i ../avro/measurements.avsc -o cpx.hh -n a
```

Then follow default build routine described in hw assignment: https://cw.fel.cvut.cz/wiki/courses/b4m36esw/labs/lab06