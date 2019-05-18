//  Created by react-native-create-bridge

package com.javascriptmeetup.crybaby;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CryBabyPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
      // Register your native module
      // https://facebook.github.io/react-native/docs/native-modules-android.html#register-the-module
      return Arrays.<NativeModule>asList(
          new CryBabyModule(reactContext)
      );
    }

    
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
