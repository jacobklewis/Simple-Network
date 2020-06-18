package me.jacoblewis.network

import me.jacoblewis.network.models.Request
import me.jacoblewis.network.models.Response
import me.jacoblewis.network.utils.BaseCallable

class NetworkTask(
    private val request: Request,
    private val onComplete: (Response) -> Unit,
    private val onProgress: ((progress: Float) -> Unit)?
) : BaseCallable<Response> {

    override fun call(): Response {
        return NetworkJob.run(request, null)
    }

    override fun setDataAfterLoading(data: Response?) {
        data?.let { onComplete(it) } ?: TODO()
    }

//    override fun doInBackground(vararg requests: Request): Response {
//
//    }
//
//    override fun onProgressUpdate(vararg values: Float?) {
//        values.firstOrNull()?.let {
//            onProgress?.invoke(it)
//        }
//    }

//    override fun onPostExecute(result: Response) {
//        onComplete(result)
//    }
}