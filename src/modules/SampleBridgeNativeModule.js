//  Created by react-native-create-bridge

import { NativeModules } from 'react-native'

const { SampleBridge } = NativeModules

export default {
  setInterval (interval) {
    return SampleBridge.setInterval(interval);
  },

  EXAMPLE_CONSTANT: SampleBridge.EXAMPLE_CONSTANT
}
