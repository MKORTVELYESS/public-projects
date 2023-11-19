import fs from "node:fs";
import * as constant from "./constants.js";
export function saveLastPost(lastPostPath, lastPostText) {
  fs.writeFileSync(lastPostPath, lastPostText);
}
export function getLastPostIndex(currentPostList) {
  const lastPostText = fs.readFileSync(constant.$LAST_FILEPATH);
  const result = currentPostList.findIndex((item) =>
    item.includes(lastPostText)
  );
  if (result !== -1) {
    return currentPostList.findIndex((item) => item.includes(lastPostText));
  } else {
    throw new Error("Last post index not found");
  }
}

export function saveResult(data, prompt, limitPerFile) {
  for (let i = 0; i < getSplitCount(data, limitPerFile); i++) {
    const start = i * 30;
    const end = (i + 1) * 30;

    const slice = data.slice(start, end);

    const mergedData = slice.join(";");
    const output = prompt + mergedData;

    fs.writeFileSync(`${constant.$OUTPUT_FILEPATH + i}.txt`, output);
  }
}

const getSplitCount = (arr, limit) => Math.ceil(arr.length / limit);

export function cleanupResults(folderPath) {
  const files = fs.readdirSync(folderPath);
  files.forEach((file) => {
    const filePath = path.join(folderPath, file);
    fs.unlinkSync(filePath);
  });
}
