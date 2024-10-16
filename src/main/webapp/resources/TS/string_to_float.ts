import {Result} from "./result.js"

const floating_point_number_regex = /^[+-]?\d+([.,]\d*)?$/;

export function stringToFloat(x: string) : Result<number, string>
{
    if (floating_point_number_regex.test(x)) {
        x = x.replace(',', '.');
        return Result.success(parseFloat(x));
    }

    return Result.failure("Input does not represent a floating point number");
}
