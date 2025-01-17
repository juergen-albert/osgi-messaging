/*******************************************************************************
 * Copyright 2020-2023 Amit Kumar Mondal
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package in.bytehue.messaging.mqtt5.provider;

import static org.awaitility.Awaitility.await;

import java.util.Collections;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import aQute.launchpad.Launchpad;

public final class TestHelper {

	/**
	 * Non-instantiable
	 */
	private TestHelper() {
		throw new IllegalAccessError("Non-Instantiable");
	}

	/**
	 * Wait 20 seconds for the flag to be true
	 *
	 * @param flag the flag
	 * @throws InterruptedException if the thread becomes interrupted
	 */
	public static void waitForRequestProcessing(final AtomicBoolean flag) throws InterruptedException {
		await().atMost(20, TimeUnit.SECONDS).untilTrue(flag);
	}

	/**
	 * Converts dictionary to map
	 *
	 * @param dictionary the dictionary to convert
	 * @return the converted map
	 */
	public static Map<String, Object> toMap(final Dictionary<String, Object> dictionary) {
		final List<String> keys = Collections.list(dictionary.keys());
		return keys.stream().collect(Collectors.toMap(Function.identity(), dictionary::get));
	}

	public static void waitForMqttConnectionReady(final Launchpad launchpad) {
		await().atMost(20, TimeUnit.SECONDS) //
				.until((Callable<Boolean>) () -> launchpad.getService(Object.class, "(mqtt.connection.ready=true)")
						.isPresent());
	}

}
