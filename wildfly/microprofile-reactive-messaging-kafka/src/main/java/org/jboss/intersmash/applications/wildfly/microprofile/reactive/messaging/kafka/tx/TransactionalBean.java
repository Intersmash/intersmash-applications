/*
 * Copyright (C) 2021 Red Hat, Inc.
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
package org.jboss.intersmash.applications.wildfly.microprofile.reactive.messaging.kafka.tx;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Taken from WildFly testsuite, see
 * org.wildfly.test.integration.microprofile.reactive.messaging.kafka.tx.TransactionalBean.
 */
@ApplicationScoped
public class TransactionalBean {

	@PersistenceContext(unitName = "test")
	EntityManager em;

	@Transactional
	public void storeValue(String name) {
		ContextEntity entity = new ContextEntity();
		entity.setName(name);
		em.persist(entity);
	}

	@Transactional
	public Set<String> getDbRecords() {
		TypedQuery<ContextEntity> query = em.createQuery("SELECT c from ContextEntity c", ContextEntity.class);
		return query.getResultList().stream().map(v -> v.getName()).collect(Collectors.toSet());
	}

	@Transactional
	public int getCount() {
		TypedQuery<Long> query = em.createQuery("SELECT count(c) from ContextEntity c", Long.class);
		List<Long> result = query.getResultList();
		return result.get(0).intValue();
	}
}
