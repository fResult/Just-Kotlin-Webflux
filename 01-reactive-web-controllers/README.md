# Reactive Web Controllers

Writing Reactive Web Controllers

## API Usage

### GET `/api/employees`

```bash
curl localhost:8080/api/employees
```

### POST `/api/employees`

```bash
curl --location 'localhost:8080/api/employees' \
    --request POST \
    --header 'Content-Type: application/json' \
    --data '{
      "name": "Thomas A. Anderson",
      "role": "Hacker & The One"
    }'
```
