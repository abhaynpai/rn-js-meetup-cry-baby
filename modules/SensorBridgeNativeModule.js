import { NativeModules } from 'react-native';

const { SensorBridge } = NativeModules;

export default {
  exampleMethod() {
    return SensorBridge.exampleMethod();
  },

  EXAMPLE_CONSTANT: SensorBridge.EXAMPLE_CONSTANT,
};
