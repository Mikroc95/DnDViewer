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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val itemRepository: ItemsRepository,
    private val spellRepository: SpellRepository
) : ViewModel() {

    private var _charactersFlow = MutableStateFlow<List<CharacterModel>>(emptyList())
    var charactersFlow: StateFlow<List<CharacterModel>> = _charactersFlow.asStateFlow()

    private var _selectedCharacter = MutableStateFlow<CharacterModel>(CharacterModel())
    var selectedCharacter: StateFlow<CharacterModel> = _selectedCharacter.asStateFlow()

    private var _itemsFlowList = MutableStateFlow<List<ItemsModel>>(emptyList())
    var itemsFlowList: StateFlow<List<ItemsModel>> = _itemsFlowList.asStateFlow()

    private var _consumableFlowList = MutableStateFlow<List<ItemsModel>>(emptyList())
    var consumableFlowList: StateFlow<List<ItemsModel>> = _consumableFlowList.asStateFlow()

    private var _spellsFlowList = MutableStateFlow<MutableList<SpellModel>>(mutableListOf())
    var spellsFlowList: StateFlow<MutableList<SpellModel>> = _spellsFlowList.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            characterRepository.getAllCharactersFlow().collect { characters ->
                _charactersFlow.value = characters
            }
        }
    }

    fun getCharacterSelected(character: CharacterModel) {
        viewModelScope.launch {
            _selectedCharacter.value = character
            getObjectes(character.code)
            getSpells(character.code)
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
            deleteObjectes(_selectedCharacter.value.code)
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
                item.code = insertCharacter(item).toInt()
            } else {
                updateCharacters(character = item)
                item = getCharacterByName(item.name)
            }
            getCharacterSelected(item)
            insertSpells(item.code, item.maxSpell)
            checkUselessFolder(item.name, context)
        }
    }

    suspend fun insertCharacter(character: CharacterModel): Long {
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

    fun getObjectes(characterCode: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.getItemsByCharacterCode(characterCode).collect { items ->
                _itemsFlowList.value =
                    items.filter { it.character == characterCode && !it.isConsumible }
                _consumableFlowList.value =
                    items.filter { it.character == characterCode && it.isConsumible }
            }
        }
    }

    fun insertObjectes(item: ItemsModel) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                itemRepository.insertItem(item)
                getObjectes(item.character)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateObjectes(item: ItemsModel) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                itemRepository.updateItem(item)
                getObjectes(item.character)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteObjectes(characterCode: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _itemsFlowList.value.filter { it.character == characterCode }.forEach {
                itemRepository.deleteItem(it)
            }
            _consumableFlowList.value.filter { it.character == characterCode }.forEach {
                itemRepository.deleteItem(it)
            }
            getObjectes(_selectedCharacter.value.code)
        }
    }

    fun deleteObjecte(itemsModel: ItemsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.deleteItem(itemsModel)
            getObjectes(itemsModel.character)
        }
    }

    fun getSpells(characterCode: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            spellRepository.getSpells(characterCode).collect { spells ->
                _spellsFlowList.value =
                    spells.filter { it.character == characterCode }.toMutableList()
            }
        }
    }

    fun insertSpells(characterCode: Int, numerSpells: Int) {
        try {
            var counter = _spellsFlowList.value.size
            if (counter < numerSpells) {
                while (counter < numerSpells) {
                    setSpells(item = SpellModel(character = characterCode))
                    counter++
                }
            } else {
                while (counter > numerSpells) {
                    deleteSpell(_spellsFlowList.value.last())
                    counter--
                    _spellsFlowList.value.remove(_spellsFlowList.value.last())
                }
            }
            getSpells(characterCode)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setSpells(item: SpellModel) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                spellRepository.insertSpell(item)
                getSpells(item.character)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateSpell(item: SpellModel) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                spellRepository.updateSpell(item)
                getSpells(item.character)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteSpell(spell: SpellModel) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                spellRepository.deleteSpell(spell)
                getSpells(spell.character)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteSpells(characterCode: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _spellsFlowList.value.filter { it.character == characterCode }.forEach {
                spellRepository.deleteSpell(it)
            }
            getSpells(characterCode = characterCode)
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

    fun checkUselessFolder(newItem: String, context: Context) {
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