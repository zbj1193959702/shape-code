package com.biji.puppeteer.util;


import com.biji.puppeteer.service.enums.ResultCodeEnum;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Optional.empty;

public class Result<T> implements Serializable {

	private int code;
	private String msg;
	private T data;

	public final static int SUCCESS = 200;
	public final static int ERROR = 500;

	public Result(int code) {
		this.code = code;
	}

	public static <T> Result<T> errorInstance(ResultCodeEnum e) {
		return new Result<>(ERROR, e.defaultMsg);
	}

	public static <T> Result<T> errorInstance(String msg) {
		return new Result<>(ERROR, msg);
	}
	public static <T> Result<T> successInstance(T e) {
		return new Result<>(SUCCESS, e);
	}

	public static <T> Result<T> successInstance() {
		return new Result<>(SUCCESS);
	}

	public static <T> Result<T> successInstance(T e, String msg) {
		return new Result<>(SUCCESS, msg, e);
	}

	public Result(int code, T data) {
		this.code = code;
		this.data = data;
	}
	public Result(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Result(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public boolean isSuccess(){
		return code == SUCCESS;
	}

	public void ifSuccess(Consumer<? super T> consumer) {
		if (data != null)
			consumer.accept(data);
	}

	public Optional<T> filter(Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		if (!isSuccess())
			return Optional.of(data);
		else
			return predicate.test(data) ? Optional.of(data) : empty();
	}

	public<U> Optional<U> map(Function<? super T, ? extends U> mapper) {
		Objects.requireNonNull(mapper);
		if (!isSuccess())
			return empty();
		else {
			return Optional.ofNullable(mapper.apply(data));
		}
	}

	public<U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
		Objects.requireNonNull(mapper);
		if (!isSuccess())
			return empty();
		else {
			return Objects.requireNonNull(mapper.apply(data));
		}
	}

	/**
	 * Return the value if present, otherwise return {@code other}.
	 *
	 * @param other the value to be returned if there is no value present, may
	 * be null
	 * @return the value, if present, otherwise {@code other}
	 */
	public T orElse(T other) {
		return data != null ? data : other;
	}

	/**
	 * Return the value if present, otherwise invoke {@code other} and return
	 * the result of that invocation.
	 *
	 * @param other a {@code Supplier} whose result is returned if no value
	 * is present
	 * @return the value if present otherwise the result of {@code other.get()}
	 * @throws NullPointerException if value is not present and {@code other} is
	 * null
	 */
	public T orElseGet(Supplier<? extends T> other) {
		return data != null ? data : other.get();
	}

	/**
	 * Return the contained value, if present, otherwise throw an exception
	 * to be created by the provided supplier.
	 *
	 * @apiNote A method reference to the exception constructor with an empty
	 * argument list can be used as the supplier. For example,
	 * {@code IllegalStateException::new}
	 *
	 * @param <X> Type of the exception to be thrown
	 * @param exceptionSupplier The supplier which will return the exception to
	 * be thrown
	 * @return the present value
	 * @throws X if there is no value present
	 * @throws NullPointerException if no value is present and
	 * {@code exceptionSupplier} is null
	 */
	public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
		if (data != null) {
			return data;
		} else {
			throw exceptionSupplier.get();
		}
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static int getERROR() {
		return ERROR;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static int getSUCCESS() {
		return SUCCESS;
	}
}
