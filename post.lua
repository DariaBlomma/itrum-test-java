wrk.method = "POST"
wrk.body   = '{"walletUuid": "11111111-1111-1111-1111-111111111111", "operationType": "WITHDRAW", "amount": 1}'
wrk.headers["Content-Type"] = "application/json"