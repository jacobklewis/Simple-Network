/*
 * Copyright (C) 2020 Jacob Lewis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package me.jacoblewis.network

import me.jacoblewis.network.models.Request
import me.jacoblewis.network.models.Response
import me.jacoblewis.network.utils.BaseCallable

class NetworkTask(
    private val request: Request,
    private val onComplete: (Response) -> Unit,
    private val onProgress: ((progress: Float) -> Unit)?
) : BaseCallable<Response> {

    override var loadingCallback: ((Float) -> Unit)? = null
    override val hasLoading: Boolean
        get() = onProgress != null

    override fun call(): Response {
        return NetworkJob.run(request, loadingCallback)
    }

    override fun setLoading(loadingValue: Float) {
        onProgress?.invoke(loadingValue)
    }

    override fun setDataAfterLoading(data: Response?) {
        data?.let { onComplete(it) } ?: TODO()
    }
}