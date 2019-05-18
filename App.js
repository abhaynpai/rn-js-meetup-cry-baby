/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View, DeviceEventEmitter, Dimensions } from 'react-native';
import { Container, Header, Content, Button, Body } from 'native-base';
import LottieView from 'lottie-react-native';
import CryBaby from './src/modules/CryBabyNativeModule';

const Animations = {
  WHAT_YOU_DO: require('./src/animation/what-you-do.json'),
  CRY_BLOB: require('./src/animation/cry-blob.json'),
  LOVE_BLOB: require('./src/animation/love-blob.json')
};

const BUTTON_TEXT_LABEL = {
  start: 'Start Listening',
  stop: 'Stop Listening'
};

const SCREEN_SIZE = {
  width: Dimensions.get('window').width,
  height: Dimensions.get('window').height,
}

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

type Props = {};
export default class App extends Component<Props> {

  constructor(props) {
    super(props);
    this.state = {
      listening: false,
      moving: false,
      buttonTxt: BUTTON_TEXT_LABEL.start,
      animation: Animations.WHAT_YOU_DO,
      shakeCount: 0
    };
  }

  startListening() {
    CryBaby.startListening();
    this.setState({
      listening: true,
      buttonTxt: BUTTON_TEXT_LABEL.stop,
      animation: Animations.CRY_BLOB
    });
    DeviceEventEmitter.addListener('accelerometer', (readings) => {
      console.log(readings);
      if (readings.speed > 20 && this.state.animation !== Animations.LOVE_BLOB) {
        this.setState({
          animation: Animations.LOVE_BLOB,
        });
      } else if (readings.speed < 20 && this.state.animation === Animations.LOVE_BLOB) {
        this.setState({
          animation: Animations.CRY_BLOB,
        });
      }
    });
  }

  stopListening() {
    CryBaby.stopListening();
    this.setState({
      listening: false,
      moving: false,
      buttonTxt: BUTTON_TEXT_LABEL.start,
      animation: Animations.WHAT_YOU_DO
    });
    DeviceEventEmitter.removeAllListeners('accelerometer');
  }

  onPress() {
    console.log('clicked');
    console.log(this.state);
    if (!this.state.listening) {
      return this.startListening();
    }
    return this.stopListening();
  }

  renderButton() {
    return (
      <Button style={[styles.button, { alignSelf: 'center' }]} onPress={this.onPress.bind(this)}>
        <Text style={styles.text}>{this.state.buttonTxt}</Text>
      </Button>
    );
  }

  renderAnimation() {
    return (
      <View style={{ height: 100, width: SCREEN_SIZE.width, height: SCREEN_SIZE.height / 2 }}>
        <LottieView source={this.state.animation} autoPlay loop />
      </View>
    );
  }

  render() {
    return (
      <Container style={styles.container}>
        <Header>
          <Body>
            <Text style={[styles.text, styles.header]}>Mumbai JavaScript Meetup</Text>
          </Body>
        </Header>
        <Content style={{ flexDirection: 'column', flex: 1, width: SCREEN_SIZE.width }}>
          {this.renderButton()}
          {this.renderAnimation()}
        </Content>
      </Container>
    );
  }
}

const styles = StyleSheet.create({
  text: {
    fontFamily: 'Roboto',
    color: 'white'
  },
  header: {
    fontSize: 20,
  },
  button: {
    padding: 20,
    margin: 20,
    flex: 0.5
  },
  contentView: {
    // flex: 1,
    // alignItems: 'center',
    // flexDirection: 'column',
    // height: '100%'
  },
  container: {
    fontFamily: 'Roboto',
    color: 'white'
  }
});
