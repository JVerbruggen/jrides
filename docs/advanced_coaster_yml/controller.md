# Controller

The controller attribute can create complex station controllers.

### type: simultaneous
Two stations that dispatch at the same time.

```yml
type: simultaneous
spec:
  stationLeft: station_a
  stationRight: station_b
```


### type: alternate

Two stations that cannot dispatch at the same time, but alternate.

```yml
type: alternate
spec:
  stationLeft: station_a
  stationRight: station_b
```