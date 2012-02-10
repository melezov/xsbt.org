$ ->
  $('#releases').tablesorter
    # initially sorted descending to version
    sortList:
      [[0,1]]

    # all anchor links have sorting disabled
    headers:
      3: {sorter: false}
      4: {sorter: false}
      5: {sorter: false}
      6: {sorter: false}
      7: {sorter: false}
      8: {sorter: false}
      9: {sorter: false}
      10: {sorter: false}
      11: {sorter: false}
      12: {sorter: false}
      13: {sorter: false}

  $('#show-legacy').click (e) ->
    e.preventDefault()
    $('#show-legacy').hide()
    $('.legacy').removeAttr('hidden')
    $('#hide-legacy').show()

  $('#hide-legacy').click (e) ->
    e.preventDefault()
    $('#hide-legacy').hide()
    $('.legacy').attr('hidden', 'hidden')
    $('#show-legacy').show()

  $('thead th').click ->
    $('body').scrollTo(
      $('#'+$(@).attr('rel')),500, {axis:'y'}
    ) unless $(@).children().length is 0
 