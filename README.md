# react-native-sunmi-barcode-scanner

## Getting started

`$ npm install react-native-sunmi-barcode-scanner --save`

### Mostly automatic installation

`$ react-native link react-native-sunmi-barcode-scanner`

## Usage
```javascript
import SunmiBarcodeScanner from 'react-native-sunmi-barcode-scanner';

const code = await SunmiBarcodeScanner.scan();
console.log('Received code:', code);
```
