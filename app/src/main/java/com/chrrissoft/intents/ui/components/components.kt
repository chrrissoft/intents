package com.chrrissoft.intents.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.DataObject
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.RemoveCircle
import androidx.compose.material3.*
import androidx.compose.material3.InputChipDefaults.inputChipBorder
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.chrrissoft.intents.Util.toUiAction
import com.chrrissoft.intents.custom.ui.CustomScreenState
import com.chrrissoft.intents.custom.ui.CustomScreenState.IntentBuilder.Component
import com.chrrissoft.intents.ui.theme.inputChipColors
import com.chrrissoft.intents.ui.theme.textFieldColors


@Composable
fun IntentBuilder(
    title: String,
    actions: List<String>,
    state: CustomScreenState.IntentBuilder,
    components: List<Component>,
    onChangeComponent: (Component) -> Unit,
    onAddBundleData: (Pair<String, String>) -> Unit,
    onRemoveBundleData: (String) -> Unit,
    onChangeAction: (String) -> Unit,
) {
    Container(
        title = title
    ) {
        IntentComponentsRow(
            components = components,
            selectedComponent = state.component,
            onChangeComponent = { onChangeComponent(it) },
        )

        BundleData(
            data = state.bundleData,
            onAdd = { onAddBundleData(it) },
            onRemove = { onRemoveBundleData(it) },
        )

        Actions(
            action = state.action,
            actions = actions,
            onChangeAction = {
                onChangeAction(it)
            }
        )
    }
}


@Composable
fun Actions(
    action: String,
    actions: List<String>,
    onChangeAction: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = CenterVertically,
    ) {
        actions.forEach { currentAction ->
            Spacer(modifier = Modifier.weight(.05f))
            ActionChip(
                action = currentAction.toUiAction(),
                selected = currentAction==action,
                onSelect = { onChangeAction(currentAction) },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.weight(.05f))

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionChip(
    action: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    InputChip(
        selected = selected,
        onClick = { onSelect() },
        colors = inputChipColors,
        border = inputChipBorder(borderColor = colorScheme.primary),
        label = { Text(action, style = typography.labelMedium) },
        modifier = modifier
    )
}


@Composable
fun Flags(
    tags: List<Triple<Int, String, Boolean>>,
    onToggle: (Triple<Int, String, Boolean>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (showInput, changeShowInput) = remember {
        mutableStateOf(false)
    }

    if (showInput) {
        AlertDialog(
            onDismissRequest = {
                changeShowInput(false)
            },
            confirmButton = {
                Button(onClick = { changeShowInput(false) }) {
                    Text(text = "Okay")
                }
            },
            title = {
                Text(text = "Intent Flags Builder")
            },
            icon = {
                Icon(imageVector = Rounded.Flag, contentDescription = null)
            },
            text = {
                LazyColumn {
                    items(tags) { flag ->
                        FlagChip(
                            flag = flag.second,
                            selected = flag.third,
                            onToggle = { onToggle(flag.copy(third = it)) }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            },
            containerColor = colorScheme.primaryContainer,
            titleContentColor = colorScheme.primary,
            iconContentColor = colorScheme.primary,
        )
    }
    ListContainer(
        onAdd = { changeShowInput(true) },
        modifier = modifier
    ) {
        if (tags.isEmpty()) addElementButtonChip(text = "Flags") { changeShowInput(true) }
        items(tags.toList()) { flag ->
            FlagChip(
                flag = flag.second,
                selected = flag.third,
                onToggle = { onToggle(flag.copy(third = it)) }
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FlagChip(
    flag: String,
    selected: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    InputChip(
        selected = selected,
        onClick = { onToggle(!selected) },
        colors = inputChipColors,
        border = inputChipBorder(borderColor = colorScheme.primary),
        label = { Text(flag, style = typography.labelMedium) },
        trailingIcon = {
            val icon = if (selected) Rounded.RemoveCircle else Rounded.AddCircle
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.clickable { onToggle(!selected) }
            )
        },
    )
}


@Composable
fun BundleData(
    data: Map<String, String>,
    onAdd: (Pair<String, String>) -> Unit,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (showInput, changeShowInput) = remember {
        mutableStateOf(false)
    }

    if (showInput) {
        val (key, changeKey) = remember {
            mutableStateOf("")
        }

        val (value, changeValue) = remember {
            mutableStateOf("")
        }

        AlertDialog(
            onDismissRequest = {
                changeShowInput(false)
            },
            confirmButton = {
                Button(
                    enabled = key.isNotEmpty() && value.isNotEmpty(),
                    onClick = {
                        changeKey("")
                        changeValue("")
                        onAdd(Pair(key, value))
                    },
                ) {
                    Text(text = "Add Bundle Data")
                }
            },
            title = {
                Text(text = "Add Bundle Data to App Companion")
            },
            icon = {
                Icon(
                    imageVector = Rounded.DataObject, contentDescription = null
                )
            },
            text = {
                Column {
                    TextField(
                        value = key,
                        onValueChange = changeKey,
                        colors = textFieldColors,
                        placeholder = { Text(text = "Key") },
                        modifier = Modifier.clip(MaterialTheme.shapes.medium)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = value,
                        onValueChange = changeValue,
                        colors = textFieldColors,
                        placeholder = { Text(text = "Value") },
                        modifier = Modifier.clip(MaterialTheme.shapes.medium)
                    )
                }
            },
            containerColor = colorScheme.primaryContainer,
            iconContentColor = colorScheme.primary,
            titleContentColor = colorScheme.primary,
        )
    }

    ListContainer(
        onAdd = { changeShowInput(true) }, modifier = modifier
    ) {
        if (data.isEmpty()) addElementButtonChip(text = "bundle data") { changeShowInput(true) }

        data.forEach {
            item {
                BundleDataItem(key = it.key, value = it.value) { onRemove(it.key) }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BundleDataItem(
    key: String,
    value: String,
    modifier: Modifier = Modifier,
    onRemove: (() -> Unit)? = null,
) {
    InputChip(
        selected = false,
        onClick = { },
        colors = inputChipColors,
        border = inputChipBorder(borderColor = colorScheme.primary),
        label = {
            Text("$key : $value")
        },
        trailingIcon = {
            if (onRemove!=null) {
                Icon(imageVector = Rounded.RemoveCircle,
                    contentDescription = null,
                    modifier = Modifier.clickable { onRemove() })
            }
        },
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntentComponentsRow(
    components: List<Component>,
    selectedComponent: Component,
    onChangeComponent: (Component) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (showFlags, changeShowFlags) = remember {
        mutableStateOf(false)
    }

    if (showFlags) {
        AlertDialog(
            onDismissRequest = { changeShowFlags(false) },
            text = {
                Text(text = "TODO")
            },
            confirmButton = {
                Button(onClick = { changeShowFlags(false) }) {
                    Text(text = "Okay")
                }
            },
            title = {
                Text(text = "Components Flags")
            },
            icon = {
                Icon(imageVector = Rounded.Flag, contentDescription = null)
            },
            containerColor = colorScheme.primaryContainer,
            iconContentColor = colorScheme.primary,
            titleContentColor = colorScheme.primary,
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
    ) {
        components.forEach {
            val selected = it==selectedComponent
                IntentComponent(
                    element = it,
                    selected = selected,
                    onSelected = { onChangeComponent(it) }
                )
        }
        InputChip(
            selected = showFlags,
            enabled = selectedComponent !is Component.Service,
            onClick = { changeShowFlags(true) },
            label = { Text(text = "Flags") },
            colors = inputChipColors,
            border = inputChipBorder(borderColor = colorScheme.primary),
            modifier = modifier,
            trailingIcon = {
                Icon(imageVector = Rounded.Flag, contentDescription = null)
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntentComponent(
    element: Component,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onSelected: () -> Unit,
) {
    InputChip(
        selected = selected,
        onClick = onSelected,
        label = {
            Text(text = element.label, style = typography.labelMedium)
        },
        colors = inputChipColors,
        border = inputChipBorder(borderColor = colorScheme.primary),
        modifier = modifier
    )
}


@Composable
private fun ListContainer(
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = CenterVertically,
    ) {
        IconButton(modifier = Modifier.weight(1f), onClick = { onAdd() }) {
            Icon(
                imageVector = Rounded.AddCircle, contentDescription = null
            )
        }

        LazyRow(modifier = Modifier.weight(7f)) {
            content()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
private fun LazyListScope.addElementButtonChip(
    text: String,
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
) {
    item {
        InputChip(
            selected = false,
            modifier = modifier,
            onClick = { onAdd() },
            colors = inputChipColors,
            border = inputChipBorder(borderColor = colorScheme.primary),
            label = { Text("Tap icon or here to add $text") },
            trailingIcon = {
                Icon(imageVector = Rounded.AddCircle,
                    contentDescription = null,
                    modifier = Modifier.clickable { onAdd() })
            },
        )
    }
}
