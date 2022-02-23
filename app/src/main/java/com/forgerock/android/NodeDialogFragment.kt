package com.forgerock.android

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.forgerock.android.auth.Node
import org.forgerock.android.auth.callback.NameCallback
import org.forgerock.android.auth.callback.PasswordCallback


class NodeDialogFragment: DialogFragment() {
    private var listener: MainActivity? = null
    private var node: Node? = null
    companion object {
        fun newInstance(node: Node?): NodeDialogFragment {
            return NodeDialogFragment().apply {
                arguments = Bundle().apply {
                    this.putSerializable("NODE", node)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_node, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        node = arguments?.getSerializable("NODE") as Node?
        node?.callbacks?.forEach {
            when (it.type) {
                "NameCallback" -> {
                    (view.findViewById(R.id.usernameLayout) as? TextInputLayout)?.visibility = View.VISIBLE
                }
                "PasswordCallback" -> {
                    (view.findViewById(R.id.passwordLayout) as? TextInputLayout)?.visibility = View.VISIBLE
                }
            }
        }
        val username: TextInputEditText = view.findViewById(R.id.username)
        val password: TextInputEditText = view.findViewById(R.id.password)
        val next: Button = view.findViewById(R.id.next)
        next.setOnClickListener {
            dismiss()
            node?.getCallback(NameCallback::class.java)?.setName(username.text.toString())
            node?.getCallback(PasswordCallback::class.java)?.setPassword(password.text.toString().toCharArray())
            node?.next(context, listener)
        }
        val cancel: Button = view.findViewById(R.id.cancel)
        cancel.setOnClickListener { dismiss() }
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params as? WindowManager.LayoutParams
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            listener = context
        }
    }
}
