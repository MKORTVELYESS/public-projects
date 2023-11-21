import { parse } from "node-html-parser";
function getPosts(htmlFeed, cssSelector) {
  const feed = parse(htmlFeed);
  const posts = feed.querySelectorAll(cssSelector);
  return posts;
}

export default getPosts;
