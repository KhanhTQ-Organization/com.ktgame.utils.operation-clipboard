using UnityEngine;

#if UNITY_IOS
using System.Runtime.InteropServices;
#endif

namespace com.ktgame.utils.operation_clipboard
{
	public static class Clipboard
	{
#if UNITY_IOS
		[DllImport("__Internal")]
		private static extern string GetClipBoard();

		[DllImport("__Internal")]
		private static extern void SetClipBoard(string text);
#endif

		/// <summary>
		/// Get value from clipboard
		/// </summary>
		/// <returns></returns>
		public static string GetValue()
		{
#if UNITY_ANDROID
			using var androidJavaClass = new AndroidJavaClass("com.ktgame.operationclipboard.OperationClipboard");
			return androidJavaClass.CallStatic<string>("GetClipBoard");
#elif UNITY_IOS
			return GetClipBoard();
#else
			return GUIUtility.systemCopyBuffer ?? string.Empty;
#endif
		}

		/// <summary>
		/// Set value to clipboard
		/// </summary>
		/// <param name="text"></param>
		public static void SetValue(string text)
		{
#if UNITY_EDITOR || UNITY_STANDALONE
			var textEditor = new TextEditor
			{
				text = text
			};

			textEditor.OnFocus();
			textEditor.Copy();
#elif UNITY_ANDROID
			using var androidJavaClass = new AndroidJavaClass("com.ktgame.operationclipboard.OperationClipboard");
			androidJavaClass.CallStatic("SetClipBoard", text);
#elif UNITY_IOS
			SetClipBoard(text);
#else
			GUIUtility.systemCopyBuffer = text;
#endif
		}
	}
}
