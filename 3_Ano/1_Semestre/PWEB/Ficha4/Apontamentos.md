A nivel de segurança temos de validar as transaçoes entre o frontend e a API (frameworkIdentity para login) e depois do login o uso de tokens (jwt) criados pela API
O token vai junto com o pedido e é verificado na API se corresponde ao user correto e se é um token valido
