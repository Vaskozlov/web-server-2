import {IllegalStateError} from "./illegal_state_error.js";

export class Result<V, E> {
    private readonly value: V;
    private readonly error: E;

    private constructor(value: V, error: E) {
        this.value = value;
        this.error = error;
    }

    public static success<V, E>(value: V): Result<V, E> {
        return new Result<V, E>(value, null);
    }

    public static failure<V, E>(error: E): Result<V, E> {
        return new Result<V, E>(null, error);
    }

    public isSuccess(): boolean {
        return this.value !== null;
    }

    public isFailure(): boolean {
        return !this.isSuccess();
    }

    public getValue(): V {
        if (this.isSuccess()) {
            return this.value;
        }

        throw new IllegalStateError("Cannot get value from failure result");
    }

    public getError() : E {
        if (this.isFailure()) {
            return this.error;
        }

        throw new IllegalStateError("Cannot get error from success result");
    }
}