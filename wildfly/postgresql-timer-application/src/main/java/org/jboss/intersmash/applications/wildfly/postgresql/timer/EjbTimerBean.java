/**
 * Copyright (C) 2026 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.intersmash.applications.wildfly.postgresql.timer;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.ejb.Timeout;
import jakarta.ejb.Timer;
import jakarta.ejb.TimerService;
import java.io.Serializable;
import java.util.logging.Logger;

@Stateless
public class EjbTimerBean {

	private static final Logger logger = Logger.getLogger(EjbTimerBean.class.getName());

	protected static int delay = 20000;
	private Serializable info = "EjbTimerBean";

	@Resource
	protected TimerService timerService;

	public void createTimer() {
		logger.info("Creating timer " + info);
		this.timerService.createTimer(delay, info);
	}

	@Timeout
	public void timeout(Timer t) {
		logger.info("Timeout of timer " + t.getInfo());
	}
}
