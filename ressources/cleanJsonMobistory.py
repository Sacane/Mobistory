import jsonlines
import json
import emoji

##################################################FUNCTIONS###############################################################

def remove_emojis(input_string):
    result_string = input_string
    for e in emoji.emoji_list(result_string):
        result_string = result_string.replace(e['emoji'], '')
    return result_string


def label_to_dictionary(label, newKeyName, failIfExists=False):
	dictionary = dict()
	if len(label) != 0:
		for value in label.split('||'):
			parts = value.split(':')
			if newKeyName in dictionary:
				if failIfExists :
					print("[ERROR] multiple values for", record['id'], "on field :", newKeyName)
				else :
					for value in "".join(parts[1:]).split(';'):
						value = remove_emojis(value)
						if len(value) > 0:
							dictionary[parts[0]].append(value)
			else:
				if failIfExists:
					value = remove_emojis("".join(parts[1:]))
					dictionary[parts[0]] = value
				else:
					firstValue = True
					for value in "".join(parts[1:]).split(';'):
						value = remove_emojis(value)
						if len(value) > 0:
							if firstValue:
								dictionary[parts[0]] = [value]
								firstValue = False
							else:
								dictionary[parts[0]].append(value)
	return dictionary


def claimToKeyWithUniqueValue(record, value, newSectionName):
	if newSectionName in record:
		print("[ERROR] multiple values for", record['id'], "on field :", newSectionName)
	else:
		record[newSectionName] = value


def claimToKeyWithValueList(record, value, newSectionName):
	if newSectionName in record:
		record[newSectionName].append(value)
	else:
		record[newSectionName] = [value]


def claimToKeyWithLabelList(record, value, newSectionName):
	if newSectionName in record:
		record[newSectionName] = concat(label_to_dictionary(value, newSectionName), record[newSectionName])
	else:
		record[newSectionName] = label_to_dictionary(value, newSectionName)


def concat(dict1, dict2):
	result = dict()
	for key in set(dict1) | set(dict2):
		result[key] = dict1.get(key, []) + dict2.get(key, [])
	return result

##################################################FORMAT SIMPLE###############################################################

def tryDeleteSourceId(record):
    if 'sourceId' in record:
        del record['sourceId']


def formatLabel(record):
	record['label'] = label_to_dictionary(record['label'], 'label', True)


def formatAliases(record):
	if 'aliases' in record:
		if len(record['aliases']) != 0:
			record['aliases'] = label_to_dictionary(record['aliases'], 'aliases')



def formatDescription(record):
	if 'description' in record:
		if len(record['description']) != 0:
			record['description'] = label_to_dictionary(record['description'], 'description', True)
		else:
			del record['description']


def formatWikipedia(record):
	if 'wikipedia' in record:
		if len(record['wikipedia']) != 0:
			record['wikipedia'] = label_to_dictionary(record['wikipedia'], 'wikipedia', True)
			for key in record['wikipedia']:
				record['wikipedia'][key] = 'https://fr.wikipedia.org/wiki/' + record['wikipedia'][key]
		else:
			del record['wikipedia']


##################################################FORMAT CLAIMS###############################################################

def onlyKeepWantedIdsInClaims(record, claims_id_to_keep):
    record['claims'] = [dictionary for dictionary in record['claims'] if dictionary['id'] in claims_id_to_keep]



def formatClaims(record):
	for dictionary in record['claims'] :
		id = dictionary['id']

		if  id == 580 :
			claimToKeyWithUniqueValue(record, dictionary['value'].split(':')[1], 'start_date')

		elif id == 582 :
			claimToKeyWithUniqueValue(record, dictionary['value'].split(':')[1], 'end_date')

		elif id == 585 :
			claimToKeyWithValueList(record, dictionary['value'].split(':')[1], 'dates')

		elif id == 373 :
			claimToKeyWithUniqueValue(record, dictionary['formatterUrl'].replace("$1", dictionary['value'].split(':')[1]), 'diaporama')

		elif id == 17 :
			claimToKeyWithLabelList(record, dictionary['item']['label'], 'countries')
			if 'aliases' in dictionary['item']:
				claimToKeyWithLabelList(record, dictionary['item']['aliases'], 'keywords')

		elif id == 18:
			claimToKeyWithValueList(record, dictionary['formatterUrl'].replace("$1", dictionary['value'].split(':')[1]), 'images')

		elif id in [279, 361, 921, 527]:
			claimToKeyWithLabelList(record, dictionary['item']['label'], 'keywords')
			if 'aliases' in dictionary['item']:
				claimToKeyWithLabelList(record, dictionary['item']['aliases'], 'keywords')

		elif id in [710, 1891]:
			claimToKeyWithLabelList(record, dictionary['item']['label'], 'participants')
			if 'aliases' in dictionary['item']:
				claimToKeyWithLabelList(record, dictionary['item']['aliases'], 'participants')

		elif id == 31:
			claimToKeyWithLabelList(record, dictionary['item']['label'], 'type')
			if 'aliases' in dictionary['item']:
				claimToKeyWithLabelList(record, dictionary['item']['aliases'], 'keywords')

		elif id == 276:
			claimToKeyWithLabelList(record, dictionary['item']['label'], 'locations')
			if 'claims' in dictionary['item']:
				for subDictionary in dictionary['item']['claims']:
					if subDictionary['id'] == 625 :
						claimToKeyWithValueList(record, subDictionary['value'].split(':')[1], 'coords')



############################################################################################################

def process(record, claims_id_to_keep):
    tryDeleteSourceId(record)
    formatLabel(record)
    formatAliases(record)
    formatDescription(record)
    formatWikipedia(record)
    onlyKeepWantedIdsInClaims(record, claims_id_to_keep)
    formatClaims(record)

def filterType(record, types_to_remove):
	if 'type' in record:
		for lst in record['type'].values():
			for type in lst :
				for word in types_to_remove :
					if word in type:
						return False
	return True

def filterLabel(record, label_to_remove):
	for label in record['label'].values():
		for word in label_to_remove :
			if word in label:
				return False
	return True

    
###########################################################################################################

label_to_remove = ['ministre de l\'Agriculture', 'élections municipales', 'élections législatives', 'FIAC', 'législature', 'à la radio en France', 'France aux Jeux méditerranéens', 'LGBT']
types_to_remove = ['banknotes of French franc', 'coin type', 'sporting event', 'compétition sportive', 'Salon', 'exposition', 'élection', 'elections', 'election', 'cycling team season', 'Wikimedia category', 'multi-sport event', 'FIM CEV Moto3', 'exposition temporaire', 'atelier', 'web conference', 'cérémonie', 'music festival edition']
claims_id_to_keep = [18, 373, 361, 17, 710, 279, 31, 580, 582, 585, 921, 527, 276, 1891]

###########################################################################################################

exclude = []
firstRecord = True

with jsonlines.open('events.txt', 'r') as file:
	with open('newEvents.json', 'w') as output_file:
		output_file.write('[')
		for record in file:
			if len(record['label']) == 0:
				continue
			process(record, claims_id_to_keep)
			del record['claims']
			if filterType(record, types_to_remove) and filterLabel(record, label_to_remove):
				if not firstRecord:
					output_file.write(',\n')
				else:
					firstRecord = False
				json.dump(record, output_file)
		output_file.write(']')
