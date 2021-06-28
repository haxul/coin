"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const app = express_1.default();
const port = 8080; // default port to listen
// define a route handler for the default home page
app.get("/", (request, response) => {
    var _a;
    let user = {
        name: "hel",
        age: null
    };
    const age = (_a = user.age) !== null && _a !== void 0 ? _a : 100;
    response.send("hello : " + age);
});
// start the Express server
app.listen(port, () => {
    console.log(`server started at http://localhost:${port}`);
});
//# sourceMappingURL=main.js.map