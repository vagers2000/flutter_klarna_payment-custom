class KlarnaPaymentRequest {
  final String clientToken;
  final String returnUrl;
  final String loadData;

  KlarnaPaymentRequest({
    required this.clientToken,
    required this.returnUrl,
    required this.loadData,
  });

  Map<String, String> toCreationParams() {
    return {
      'clientToken': clientToken,
      'returnUrl': returnUrl,
      'loadData': loadData,
    };
  }
}
