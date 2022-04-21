#!/bin/bash

echo -n "$1" | openssl dgst -sha256 -binary | openssl base64