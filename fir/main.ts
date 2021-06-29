import {Request, Response} from "express"
import express from "express"
import Block from "./block";

const app = express()
const port = 8080

app.get("/", (request: Request, response: Response) => {
    response.send("hello");
});

app.listen(port, () => {
    console.log(`server started at http://localhost:${port}`)
});
