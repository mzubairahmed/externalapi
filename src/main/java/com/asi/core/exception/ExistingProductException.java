package com.asi.core.exception;

import com.asi.service.product.exception.BaseException;

public class ExistingProductException extends BaseException {

		/**
	 * 
	 */
	private static final long serialVersionUID = 6586051042026876986L;

		public ExistingProductException(String productID) {
			super(productID);
			this.errorCode = "error.existing.priduct.id";
		}
		
		/**
		 * 
		 */



	}
