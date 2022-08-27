My take on popular case:  
"let's send a link in the email that will redirect to some page with data"

I would like it to be reasonably secure, with the expiry date and some way to invalidate it.

You should create file: src/main/resources/secrets.yml
and put all of the missing values of secrets there.

```yaml
spring:
  mail:
    username: # email address
    password: # email application password
  datasource:
    hikari:
      username: # db username
      password: # db password
encryption:
  secret: # a secret that will be used for AES encryption
```
