package com.xzyler.microservices.securityservice.exceptions;

import com.xzyler.microservices.securityservice.util.ApiError;
import com.xzyler.microservices.securityservice.util.CustomMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private final CustomMessageSource customMessageSource;

    public CustomRestExceptionHandler(CustomMessageSource customMessageSource) {
        this.customMessageSource = customMessageSource;
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            switch (error.getCode()) {
                case "NotNull":
                    errors.add(customMessageSource.get("error.method.argument.notnull", error.getField()));
                    break;
                case "Pattern":
                    errors.add(customMessageSource.get("error.method.argument.pattern2", error.getField(), error.getDefaultMessage()));
                    break;
                default:
                    errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus, ex.getClass().getName(), errors);
        return handleExceptionInternal(ex, apiError, headers, httpStatus, request);
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {

            errors.add(customMessageSource.get(error.getDefaultMessage(), customMessageSource.get(error.getField()), error.getArguments().length >= 2 ? error.getArguments()[1] : null));
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }


        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus, errors.get(0), errors);
        return handleExceptionInternal(ex, apiError, headers, httpStatus, request);
    }


    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final String error = customMessageSource.get("error.request.part.missing", ex.getRequestPartName());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final String error = customMessageSource.get("error.request.parameter.missing", ex.getParameterName());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    //
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final String error = customMessageSource.get("error.method.argument.mismatch", ex.getName(), ex.getRequiredType().getName());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    // 401
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> accessDenied(final AccessDeniedException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        final ApiError apiError = new ApiError(false, httpStatus, ex.getLocalizedMessage(), ex.getLocalizedMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseBody
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
//            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
            errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
        }
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({TransactionSystemException.class})
    @ResponseBody
    public ResponseEntity<Object> handleTransactionSystem(final TransactionSystemException e, final WebRequest request) {
        logger.info(e.getClass().getName());
        if (e.getRootCause() instanceof ConstraintViolationException) {
            ConstraintViolationException ex = (ConstraintViolationException) e.getRootCause();
            final List<String> errors = new ArrayList<String>();
            for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
                if (violation.getMessage().contains("null"))
                    errors.add(customMessageSource.get("error.doesn't.exist", violation.getPropertyPath()));
                else
                    errors.add(violation.getPropertyPath() + " " + violation.getMessage());

            }
            HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
            final ApiError apiError = new ApiError(false, httpStatus, ex.getLocalizedMessage(), errors);
            return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
        }
        return this.handleAll(e, request);
    }


    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseBody
    public ResponseEntity<Object> handleConstraintViolation(DataIntegrityViolationException ex, WebRequest request) {
        ex.printStackTrace();
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            logger.info(ex.getClass().getName());
            org.hibernate.exception.ConstraintViolationException violation = ((org.hibernate.exception.ConstraintViolationException) ex.getCause());
            //
            final List<String> errors = new ArrayList<String>();
//            errors.add("violates constraint "+violation.getConstraintName());
            System.out.println(violation.getCause().getLocalizedMessage());
            System.out.println(violation.getConstraintName());
            if (violation.getConstraintName().contains("unique_") || violation.getConstraintName().contains("_unique") ||
                    violation.getConstraintName().contains("uk_") || violation.getConstraintName().contains("_uk"))
                if (violation.getConstraintName().contains("unique_") || violation.getConstraintName().contains("uk_"))
//                    errors.add(customMessageSource.get("error.already.exist", plainFormatter(violation.getCause().getLocalizedMessage().split("Detail:")[1])));
                    errors.add(customMessageSource.get("error.already.exist"));
//                else if (violation.getConstraintName().contains("_uk"))
//                    errors.add(customMessageSource.get("error.already.exist", violation.getConstraintName().split("_unique")[1]));
//                else
//                    errors.add(customMessageSource.get("error.already.exist", violation.getConstraintName().split("_unique")[1]));
            else if (violation.getConstraintName().contains("_check") || violation.getConstraintName().contains("check_"))
                errors.add(customMessageSource.get("error.check.constraint", violation.getConstraintName().split("_check")[0]));
            else if (violation.getCause().getLocalizedMessage().contains("not-null"))
                errors.add(customMessageSource.get("error.doesn't.exist", violation.getConstraintName()));
            else if (violation.getCause().getLocalizedMessage().contains("is still referenced")) {
                Matcher matcher = Pattern.compile("fk_(.*?)_[a-zA-Z]+_id").matcher(violation.getConstraintName());
                errors.add(customMessageSource.get("could.not.delete", matcher.find() ? matcher.group(1) : ""));
            } else if (violation.getCause().getLocalizedMessage().contains("violates foreign key constraint")) {
                errors.add(violation.getCause().getLocalizedMessage().contains("Detail:") ? violation.getCause().getLocalizedMessage().split("Detail:")[1] : violation.getCause().getLocalizedMessage());
            } else
                errors.add(violation.getCause().getLocalizedMessage().contains("Detail:") ? formatMessage(violation.getCause().getLocalizedMessage().split("Detail: Key")[1]) : violation.getCause().getLocalizedMessage());
            HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
            final ApiError apiError = new ApiError(false, httpStatus, errors.get(0), errors);
            return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
        }
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus, ex.getClass().getName(), customMessageSource.get("error.database.error"));
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({BadClientCredentialsException.class})
    @ResponseBody
    public ResponseEntity<Object> handleTransactionSystem(final BadClientCredentialsException e, final WebRequest request) {
        logger.info(e.getClass().getName());
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        final ApiError apiError = new ApiError(false, httpStatus, e.getLocalizedMessage(), e.getLocalizedMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }


    private String formatMessage(String s) {
        String[] splittedMessage = s.split("=");
        String message = null;
        if (splittedMessage.length > 1) {
            if (splittedMessage[1].contains("already exists")) {
                message = customMessageSource.get("error.already.exist", customMessageSource.get(splittedMessage[0].replace("(", "").replace(")", "")));
            } else {
                message = splittedMessage[1];
            }
        }
        return message;
    }

    private String plainFormatter(String s) {
        String[] splittedMessage = s.split("=");
        String message = null;
        if (splittedMessage.length > 1) {
            if (splittedMessage[1].contains("already exists")) {
                message = splittedMessage[1].replace("(", "").replace(")", "").replace("already exists.", "");
            } else {
                message = splittedMessage[1];
            }
        }
        return message;
    }


    // 404
    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        final ApiError apiError = new ApiError(false, httpStatus, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    // 405
    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
        HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        final ApiError apiError = new ApiError(false, httpStatus, ex.getLocalizedMessage(), builder.toString());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    // 415
    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));
        HttpStatus httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        final ApiError apiError = new ApiError(false, httpStatus, ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    // 500
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        logger.error("error", ex);
        //
        ex.printStackTrace();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final ApiError apiError = new ApiError(false, httpStatus, ex.getLocalizedMessage(), ex.getLocalizedMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        return super.handleAsyncRequestTimeoutException(ex, headers, status, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.info(ex.getClass().getName());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Class<? extends Throwable> exception = ex.getCause().getClass();
        Object value = exception.getFields();
        final ApiError apiError = new ApiError(false, httpStatus, ex.getCause().getCause().getLocalizedMessage(), ex.getCause().getCause().getLocalizedMessage());
        return handleExceptionInternal(ex, apiError, headers, httpStatus, request);
    }


}
