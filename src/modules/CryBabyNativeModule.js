import { NativeModules } from 'react-native';

const { SensorBridge } = NativeModules;

export default {
  startListening() {
    return SensorBridge.startListening();
  },

  stopListening() {
    return SensorBridge.stopListening();
  },

  EXAMPLE_CONSTANT: SensorBridge.EXAMPLE_CONSTANT,
};
