import fs from "node:fs";
import getPosts from "./getPosts.js";
import getPostText from "./getPostText.js";
import * as help from "./helpers.js";
import * as constant from "./constants.js";

const data = fs.readFileSync(constant.$FEED_FILEPATH);

const posts = getPosts(data, constant.$POST_SELECTOR);

const postTextsFull = posts.map((post) => {
  return getPostText(post);
});

const postTextsNew = postTextsFull.slice(
  0,
  help.getLastPostIndex(postTextsFull)
);

help.saveResult(postTextsNew, constant.$PROMPT, constant.$LIMIT);
help.saveLastPost(
  constant.$LAST_FILEPATH,
  postTextsFull[help.getLastPostIndex(postTextsFull)]
);
