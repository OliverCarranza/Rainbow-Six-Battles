using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WhileLoopScript : MonoBehaviour
{
    //Awake method
    void Awake()
    {
        Debug.Log("I am awake to wash cups!");
    }

    // Start is called before the first frame update
    void Start()
    {
        int cupsInTheSink = 4;
        while (cupsInTheSink > 0)
        {
            Debug.Log("I have washed a cup!\n");
            cupsInTheSink--;
        }
    }

}
