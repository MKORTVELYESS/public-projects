import fs from "node:fs";
import getPosts from "./services/getPosts.js";
import getPostText from "./services/getPostText.js";
import * as help from "./utils/helpers.js";
import * as constant from "./utils/constants.js";

help.cleanupResults(constant.$RESULT_FOLDER);

const data = fs.readFileSync(constant.$FEED_FILEPATH);

const posts = getPosts(data, constant.$POST_SELECTOR);

const postTextsFull = posts.map((post) => {
  return getPostText(post);
});

const postTextsNew = postTextsFull.slice(
  0,
  help.getLastPostIndex(postTextsFull)
);
postTextsNew.pop();

help.saveResult(postTextsNew, constant.$PROMPT, constant.$LIMIT);
help.saveLastPost(constant.$LAST_FILEPATH, postTextsFull[0]);
