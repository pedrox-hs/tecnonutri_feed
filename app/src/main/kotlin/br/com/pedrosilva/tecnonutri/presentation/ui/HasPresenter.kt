package br.com.pedrosilva.tecnonutri.presentation.ui

import br.com.pedrosilva.tecnonutri.presentation.presenters.base.BasePresenter

interface HasPresenter {
    val presenter: BasePresenter<*>
}