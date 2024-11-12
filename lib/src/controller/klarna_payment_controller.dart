import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_klarna_payment/flutter_klarna_payment.dart';
import 'package:flutter_klarna_payment/src/controller/klarna_payment_controller_state.dart';
import 'package:rxdart/rxdart.dart';

class KlarnaPaymentController {
  KlarnaPaymentController() {
    _setupSubscription();
  }

  final _eventChannel = const EventChannel("flutter_klarna_payment_event");
  final _flutterKlarnaPaymentPlugin = FlutterKlarnaPayment();
  late final StreamSubscription _subscription;
  final _stateController = BehaviorSubject<KlarnaPaymentControllerState>();

  void pay() {
    _flutterKlarnaPaymentPlugin.pay();
  }

  void dispose() {
    _subscription.cancel();
    _stateController.close();
  }

  void resetStream() {
    _subscription.cancel();
    _stateController.close();
    _setupSubscription();
  }

  KlarnaPaymentControllerState _currentState = const KlarnaPaymentControllerState(state: KlarnaState.unknown);

  // ignore: unnecessary_getters_setters
  KlarnaPaymentControllerState get currentState => _currentState;

  // Convert the stream to a broadcast stream
  Stream<KlarnaPaymentControllerState> get stateStream => _stateController.stream;

  void _setupSubscription() {
    _subscription = _eventChannel.receiveBroadcastStream().listen((event) {
      _updateState(event['state'], event['message']);
    });
  }

  set currentState(KlarnaPaymentControllerState state) {
    _currentState = state;
  }

  void _updateState(String state, String? message) {
    final newState = KlarnaPaymentControllerState(state: klarnaStateFromString(state), message: message);
    _stateController.sink.add(newState);
    currentState = newState;
  }
}
