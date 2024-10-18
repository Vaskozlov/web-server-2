import {Plot} from "./plot.js"
import {getAllCheckboxesByClass} from "./query.js"

const host_name: string = "localhost";
const host_port: string = "8080";
const host_base_path: string = "test";

export const main_plot: Plot = new Plot("box1");
export const server_base_url: string = `http://${host_name}:${host_port}/${host_base_path}`;

export const r_checkboxes = getAllCheckboxesByClass("r_value_checkbox");
