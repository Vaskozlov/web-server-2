import {createBoard, drawAreas} from "./board.js";

const board = createBoard("box1");

window.onload = () => drawAreas(board);
