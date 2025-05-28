package com.Mikroc.DnDViewer.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Mikroc.DnDViewer.bbdd.Repository.Character.CharacterRepository
import com.Mikroc.DnDViewer.bbdd.Repository.Items.ItemsRepository
import com.Mikroc.DnDViewer.bbdd.Repository.Spell.SpellRepository
import com.Mikroc.DnDViewer.Models.CharacterModel
import com.Mikroc.DnDViewer.Models.ItemsModel
import com.Mikroc.DnDViewer.Models.SpellModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val itemRepository: ItemsRepository,
    private val spellRepository: SpellRepository
) : ViewModel() {

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    private var _charactersFlow = MutableStateFlow<List<CharacterModel>>(emptyList())
    var charactersFlow: StateFlow<List<CharacterModel>> = _charactersFlow.asStateFlow()

    private var _selectedCharacter = MutableStateFlow(CharacterModel())
    var selectedCharacter: StateFlow<CharacterModel> = _selectedCharacter.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    var itemsFlowList: StateFlow<List<ItemsModel>> = selectedCharacter.flatMapLatest { character ->
        if (character.code.toString().isNotEmpty()) {
            itemRepository.getItems(character.code)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    var consumableFlowList: StateFlow<List<ItemsModel>> =
        selectedCharacter.flatMapLatest { character ->
            if (character.code.toString().isNotEmpty()) {
                itemRepository.getConsumables(character.code)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val spellsFlowList: StateFlow<List<SpellModel>> = selectedCharacter.flatMapLatest { character ->
        if (character.code.toString().isNotEmpty()) {
            spellRepository.getSpells(character.code)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            characterRepository.getAllCharactersFlow().collect { characters ->
                _charactersFlow.value = characters
            }
        }
    }
    fun setSelectedTabIndex(index: Int) {
        _selectedTabIndex.value = index
    }

    fun getCharacterSelected(character: CharacterModel) {
        viewModelScope.launch {
            _selectedCharacter.value = character
        }
    }

    suspend fun getCharacter(characterCode: Int): CharacterModel {
        return characterRepository.getCharacterById(id = characterCode)!!
    }


    fun getCharacterByName(characterName: String): CharacterModel {
        return _charactersFlow.value.firstOrNull { it.name == characterName } ?: CharacterModel()
    }

    fun deleteCharacter(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            characterRepository.deleteCharacter(_selectedCharacter.value)
            deleteItems(_selectedCharacter.value.code)
            deleteSpells(_selectedCharacter.value.code)
            deleteHomeBrew(
                characterName = _selectedCharacter.value.name,
                homebrewRoute = _selectedCharacter.value.homebrewRoute,
                context = context
            )
            if (_selectedCharacter.value.code == _selectedCharacter.value.code) {
                _selectedCharacter.value = CharacterModel()
            }
        }
    }

    fun saveOrUpdateCharacter(character: CharacterModel, isNew: Boolean, context: Context) {
        viewModelScope.launch {
            var item = character
            if (isNew) {
                val newId = insertCharacter(item)
                item = item.copy(code = newId.toInt())
            } else {
                updateCharacters(character = item)
            }
            insertSpells(item.code, item.maxSpell)
            getCharacterSelected(item)
            checkUselessFolder(item.name, context)
        }
    }

    private suspend fun insertCharacter(character: CharacterModel): Long {
        return characterRepository.insertCharacter(character)
    }

    fun updateCharacters(character: CharacterModel) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                characterRepository.updateCharacter(character)
                if (_selectedCharacter.value.code == character.code) {
                    _selectedCharacter.value = character
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun insertItems(item: ItemsModel) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                itemRepository.insertItem(item)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateItems(item: ItemsModel) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                itemRepository.updateItem(item)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

   private fun deleteItems(characterCode: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            itemsFlowList.value.filter { it.character == characterCode }.forEach {
                itemRepository.deleteItem(it)
            }
            consumableFlowList.value.filter { it.character == characterCode }.forEach {
                itemRepository.deleteItem(it)
            }
        }
    }

    fun deleteItem(itemsModel: ItemsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.deleteItem(itemsModel)
        }
    }


     private fun insertSpells(characterCode: Int, numberSpells: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentSpellList = spellRepository.getSpellsDirectly(characterCode)
                val currentSize = currentSpellList.size
                if (currentSize < numberSpells) {
                    val numToAdd = numberSpells - currentSize
                    repeat(numToAdd) {
                        spellRepository.insertSpell(SpellModel(character = characterCode))
                    }
                } else if (currentSize > numberSpells) {
                    val numToDelete = currentSize - numberSpells
                    val spellsToDelete = currentSpellList.takeLast(numToDelete)
                    spellsToDelete.forEach { spell ->
                        spellRepository.deleteSpell(spell)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateSpell(item: SpellModel) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                spellRepository.updateSpell(item)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun deleteSpells(characterCode: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val spellsToDelete = spellsFlowList.value.filter { it.character == characterCode }
                spellsToDelete.forEach { spell ->
                    spellRepository.deleteSpell(spell)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateCharacterHP(newHP: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentCharacter = _selectedCharacter.value
            if (currentCharacter.code == 0) return@launch

            val clampedHP = newHP.coerceIn(0, currentCharacter.vidaMax)
            if (currentCharacter.vida != clampedHP) {
                val updatedCharacter = currentCharacter.copy(vida = clampedHP)
                _selectedCharacter.value = updatedCharacter
                characterRepository.updateCharacter(updatedCharacter)
            }
        }
    }

    fun updateCharacterMana(newMana: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentCharacter = _selectedCharacter.value
            if (currentCharacter.code == 0) return@launch

            val clampedMana = newMana.coerceIn(0, currentCharacter.manaMax)
            if (currentCharacter.mana != clampedMana) {
                val updatedCharacter = currentCharacter.copy(mana = clampedMana)
                _selectedCharacter.value = updatedCharacter
                characterRepository.updateCharacter(updatedCharacter)
            }
        }
    }

    fun updateCharacterMetaMagic(newMetaMagic: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentCharacter = _selectedCharacter.value
            if (currentCharacter.code == 0) return@launch

            val clampedMetaMagic = newMetaMagic.coerceIn(0, currentCharacter.metaMagiaMax)
            if (currentCharacter.metaMagia != clampedMetaMagic) {
                val updatedCharacter = currentCharacter.copy(metaMagia = clampedMetaMagic)
                _selectedCharacter.value = updatedCharacter
                characterRepository.updateCharacter(updatedCharacter)
            }
        }
    }

    fun characterStabilized() {
        _selectedCharacter.value.let { char ->
            val updatedCharacter = char.copy(vida = 1)
            _selectedCharacter.value = updatedCharacter
            viewModelScope.launch {
                characterRepository.updateCharacter(updatedCharacter)
            }
        }
    }


    fun deleteHomeBrew(characterName: String, homebrewRoute: String, context: Context) {
        try {
            val directory = File(context.filesDir.absolutePath + "/${characterName}")
            if (directory.exists()) {
                directory.deleteRecursively()
            }
            val file = File(homebrewRoute)
            if (file.exists()) {
                file.deleteRecursively()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkUselessFolder(newItem: String, context: Context) {
        try {
            val list = mutableListOf(newItem)
            list.addAll(_charactersFlow.value.map { it.name }.toSet())
            val filesDir = context.filesDir.listFiles()

            filesDir?.forEach { item ->
                if (item.isDirectory && !list.toList().contains(item.name)) {
                    try {
                        item.deleteRecursively()
                    } catch (deleteException: Exception) {
                        deleteException.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}